docker build -t study_ms_users .
docker run --name study_ms_users --network db-net -p 8008:8080 study_ms_users
