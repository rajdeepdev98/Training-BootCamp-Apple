git clone https://github.com/rajdeepdev98/Training-BootCamp-Apple.git
cd Training-BootCamp-Apple/

cd CorporateEquipmentAllocation/Backend/Equipmentservice/
docker build -t equipmentservice:latest .

cd ..
cd MessageProcessingService/


docker build -t messageprocessingservice:latest .

cd ..
cd NotificationService/
docker build -t notificationservice:latest .