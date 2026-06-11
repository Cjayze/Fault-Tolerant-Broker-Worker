import os

# Kết nối tới RabbitMQ dựng bằng Docker
broker_url = 'amqp://guest:guest@localhost:5672//'
result_backend = 'rpc://'

# Cơ chế chịu lỗi cho Worker
task_acks_late = True             # Chỉ ACK khi đã xử lý xong hoàn toàn
worker_prefetch_multiplier = 1    # Mỗi worker chỉ nhận tối đa 1 task một lúc

# Định hình định dạng dữ liệu mã hóa
task_serializer = 'json'
result_serializer = 'json'
accept_content = ['json']
timezone = 'Asia/Ho_Chi_Minh'
enable_utc = True