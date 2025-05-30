import os
import sys
from dotenv import load_dotenv
from flask import Flask, request, jsonify
import smtplib
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText
import logging
from werkzeug.exceptions import BadRequest, UnsupportedMediaType

load_dotenv()
required_vars = [
    'SMTP_SERVER',
    'SMTP_PORT',
    'SMTP_USERNAME',
    'SMTP_PASSWORD',
    'SMTP_USE_TLS',
    'PORT'
]
missing_vars = [var for var in required_vars if not os.getenv(var)]
if missing_vars:
    print(f"Error: Missing required environment variables: {', '.join(missing_vars)}")
    sys.exit(1)

app = Flask(__name__)
app.config['PROPAGATE_EXCEPTIONS'] = True

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

SMTP_SERVER = os.getenv('SMTP_SERVER')
try:
    SMTP_PORT = int(os.getenv('SMTP_PORT'))
except ValueError:
    logger.error("SMTP_PORT must be an integer")
    sys.exit(1)
SMTP_USERNAME = os.getenv('SMTP_USERNAME')
SMTP_PASSWORD = os.getenv('SMTP_PASSWORD')
SMTP_USE_TLS = os.getenv('SMTP_USE_TLS').lower() in ['true', '1', 'yes']


@app.route('/send_email', methods=['POST'])
def send_email():
    data = request.get_json(force=True)

    subject = data.get('subject')
    body = data.get('body')
    is_html = data.get('html', False)

    try:
        msg = MIMEMultipart('alternative')
        msg['From'] = SMTP_USERNAME
        msg['To'] = ', '.join(recipients)
        msg['Subject'] = subject
        msg.attach(MIMEText(body, 'html' if is_html else 'plain'))

        server = smtplib.SMTP(SMTP_SERVER, SMTP_PORT, timeout=10)
        if SMTP_USE_TLS:
            server.starttls()
        server.login(SMTP_USERNAME, SMTP_PASSWORD)
        server.sendmail(SMTP_USERNAME, recipients, msg.as_string())
        server.quit()

        logger.info(f"Email sent to {recipients}")
        return jsonify({'status': 'Email sent successfully'}), 200

    except smtplib.SMTPException as e:
        logger.error(f"SMTP error: {e}")
        return jsonify({'error': 'Mail server error'}), 502
    except Exception as e:
        logger.exception("Unexpected error")
        return jsonify({'error': 'Internal server error'}), 500

if __name__ == '__main__':
    try:
        port = int(os.getenv('PORT'))
    except ValueError:
        logger.error("PORT must be an integer")
        sys.exit(1)
    app.run(host='0.0.0.0', port=port)