import Dao.CityDao;
import Dao.CityDaoJDBC;
import DatabaseConnection.MySQLConnection;
import Model.City;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = MySQLConnection.getConnection()) {
            CityDao cityDao = new CityDaoJDBC(connection);

            // Find by ID
            City city = cityDao.findById(1);
            System.out.println("City with ID 1: " + city);

            // Find by Country Code
            System.out.println("Cities in USA: " + cityDao.findByCode("USA"));

            // Find by Name
            System.out.println("Cities named 'Tokyo': " + cityDao.findByName("Tokyo"));

            // Find All
            System.out.println("All cities: " + cityDao.findAll());

            // Add a new City
            City newCity = new City(0, "NewCity", "USA", "NewDistrict", 123456);
            City addedCity = cityDao.add(newCity);
            System.out.println("Added City: " + addedCity);

            // Update a City
            if (addedCity != null) {
                addedCity.setPopulation(654321);
                City updatedCity = cityDao.update(addedCity);
                System.out.println("Updated City: " + updatedCity);
            }

            // Delete a City
            if (addedCity != null) {
                int rowsDeleted = cityDao.delete(addedCity);
                System.out.println("Rows Deleted: " + rowsDeleted);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
