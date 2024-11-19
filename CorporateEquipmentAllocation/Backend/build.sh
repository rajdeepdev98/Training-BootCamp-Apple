git clone https://github.com/rajdeepdev98/Training-BootCamp-Apple.git
cd Training-BootCamp-Apple/
git checkout develop
# Go to individual directories
docker build -t equipmentservice:latest .
 docker build -t messageprocessingservice:latest .
docker build -t notificationservice:latest .
# go to root folder
docker-compose up -d
#for live logs
docker logs -f container_name
