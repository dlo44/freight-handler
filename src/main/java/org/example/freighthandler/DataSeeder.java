package org.example.freighthandler;

import org.example.freighthandler.destination.Destination;
import org.example.freighthandler.door.Door;
import org.example.freighthandler.door.DoorRepository;
import org.example.freighthandler.door.DoorStatus;
import org.example.freighthandler.shipment.Shipment;
import org.example.freighthandler.shipment.ShipmentRepository;
import org.example.freighthandler.shipmentEvent.ShipmentEvent;
import org.example.freighthandler.shipmentEvent.ShipmentEventRepository;
import org.example.freighthandler.shipmentEvent.ShipmentEventType;
import org.example.freighthandler.trailer.Trailer;
import org.example.freighthandler.trailer.TrailerRepository;
import org.example.freighthandler.user.User;
import org.example.freighthandler.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile("local")
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TrailerRepository trailerRepository;
    private final DoorRepository doorRepository;
    private final ShipmentRepository shipmentRepository;
    private final ShipmentEventRepository shipmentEventRepository;

    public DataSeeder(UserRepository userRepository,
                      TrailerRepository trailerRepository,
                      DoorRepository doorRepository,
                      ShipmentRepository shipmentRepository,
                      ShipmentEventRepository shipmentEventRepository) {
        this.userRepository = userRepository;
        this.trailerRepository = trailerRepository;
        this.doorRepository = doorRepository;
        this.shipmentRepository = shipmentRepository;
        this.shipmentEventRepository = shipmentEventRepository;
    }

    @Override
    public void run(String... args) {
        seedUsers();
        seedTrailers();
        seedDoors();
        seedShipments();
    }

    private void seedUsers() {
        if (userRepository.count() == 0) {
            User user = new User();
            user.setName("Daniel");
            user.setUserId(4712376L);
            userRepository.save(user);
            System.out.println("=== SEEDED: User Daniel ===");
        }
    }

    private void seedTrailers() {
        if (trailerRepository.count() == 0) {
            Trailer unload = new Trailer();
            unload.setTrailerNumber("P420");
            unload.setDestination(Destination.NDV);
            unload.setStatus(DoorStatus.valueOf("UNLOAD"));
            unload.setLoading(false);
            trailerRepository.save(unload);

            Trailer loadNdv = new Trailer();
            loadNdv.setTrailerNumber("P421");
            loadNdv.setDestination(Destination.NDV);
            loadNdv.setStatus(DoorStatus.valueOf("LOAD"));
            loadNdv.setLoading(true);
            trailerRepository.save(loadNdv);

            Trailer loadDen = new Trailer();
            loadDen.setTrailerNumber("P422");
            loadDen.setDestination(Destination.DEN);
            loadDen.setStatus(DoorStatus.valueOf("LOAD"));
            loadDen.setLoading(true);
            trailerRepository.save(loadDen);

            System.out.println("=== SEEDED: Trailers P420, P421, P422 ===");
        }
    }

    private void seedDoors() {
        if (doorRepository.count() == 0) {
            Trailer p420 = trailerRepository.findByTrailerNumber("P420").orElse(null);
            Trailer p421 = trailerRepository.findByTrailerNumber("P421").orElse(null);
            Trailer p422 = trailerRepository.findByTrailerNumber("P422").orElse(null);

            Door door244 = new Door();
            door244.setDoorNumber(244);
            door244.setStatus(DoorStatus.valueOf("UNLOAD"));
            door244.setTrailer(p420);
            doorRepository.save(door244);

            Door door1 = new Door();
            door1.setDoorNumber(1);
            door1.setStatus(DoorStatus.valueOf("LOAD"));
            door1.setTrailer(p421);
            doorRepository.save(door1);

            Door door2 = new Door();
            door2.setDoorNumber(2);
            door2.setStatus(DoorStatus.valueOf("LOAD"));
            door2.setTrailer(p422);
            doorRepository.save(door2);

            System.out.println("=== SEEDED: Doors 244, 1, 2 ===");
        }
    }

    private void seedShipments() {
        if (shipmentRepository.count() == 0) {
            User user = userRepository.findAll().get(0);
            Trailer p420 = trailerRepository.findByTrailerNumber("P420").orElse(null);

            Object[][] shipmentData = {
                    {888842017122L, Destination.NDV, "5543 S 100 E Denver CO 80239", "5980 E 100 Pomona CA 91750"},
                    {888842017123L, Destination.NDV, "123 Main St Chicago IL 60601", "456 Oak Ave Las Vegas NV 89101"},
                    {888842017124L, Destination.NDV, "789 Pine Rd Detroit MI 48201", "321 Elm St Los Angeles CA 90001"},
                    {888842017125L, Destination.DEN, "555 Maple Ave Boston MA 02101", "999 Cedar Blvd Denver CO 80201"},
                    {888842017126L, Destination.DEN, "777 Birch St Miami FL 33101", "111 Spruce Ave Denver CO 80202"},
            };

            for (Object[] data : shipmentData) {
                Shipment shipment = new Shipment();
                shipment.setShipmentNumber((Long) data[0]);
                shipment.setDestination((Destination) data[1]);
                shipment.setShipperAddress((String) data[2]);
                shipment.setReceiverAddress((String) data[3]);
                shipment.setCurrLocation("Door 244");
                shipment.setTrailer(p420);
                Shipment saved = shipmentRepository.save(shipment);

                ShipmentEvent event = new ShipmentEvent();
                event.setShipment(saved);
                event.setPerformedBy(user);
                event.setEventType(ShipmentEventType.CREATED);
                event.setLocation("Door 244");
                event.setTrailerNumber("P420");
                event.setEventTime(LocalDateTime.now());
                shipmentEventRepository.save(event);
            }

            System.out.println("=== SEEDED: 5 Shipments on P420 at Door 244 ===");
        }
    }
}