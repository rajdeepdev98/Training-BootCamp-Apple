#git clone https://github.com/rajdeepdev98/Training-BootCamp-Apple.git
#cd Training-BootCamp-Apple/
#
#cd Event-Management
cd play-event-service
docker build -t play-event-service:latest .

cd ..
cd akka-event-service
docker build -t akka-event-service:latest .

cd ..

docker network create kafka-network


