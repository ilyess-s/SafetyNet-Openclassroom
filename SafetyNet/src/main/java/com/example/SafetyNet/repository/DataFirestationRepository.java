    package com.example.SafetyNet.repository;

    import com.example.SafetyNet.model.Firestations;
    import org.springframework.stereotype.Repository;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;

    @Repository
    public class DataFirestationRepository implements FirestationRepository {

        private List<Firestations> firestations = new ArrayList<>();

        @Override
        public List<Firestations> findAll() {
            return firestations;
        }

        @Override
        public void save(Firestations firestation) {
            firestations.add(firestation);
        }

        @Override
        public boolean update(Firestations updatedFirestation) {
            Optional<Firestations> existing = firestations.stream()
                    .filter(f -> f.getAddress().equals(updatedFirestation.getAddress()))
                    .findFirst();

            if (existing.isPresent()) {
                existing.get().setStation(updatedFirestation.getStation());
                return true;
            }
            return false;
        }

        @Override
        public boolean delete(String address) {
            return firestations.removeIf(f -> f.getAddress().equals(address));
        }
    }