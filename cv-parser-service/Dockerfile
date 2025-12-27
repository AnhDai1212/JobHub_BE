FROM python:3.10-slim

WORKDIR /app

COPY requirements.txt .
ENV PIP_EXTRA_INDEX_URL=https://download.pytorch.org/whl/cpu
RUN pip install --no-cache-dir -r requirements.txt

# Copy app code
COPY app.py .

# 🔥 COPY MODEL VÀO IMAGE (BẮT BUỘC)
COPY assets ./assets

EXPOSE 8000
CMD ["uvicorn", "app:app", "--host", "0.0.0.0", "--port", "8000"]
