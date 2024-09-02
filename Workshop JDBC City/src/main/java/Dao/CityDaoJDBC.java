package Dao;

import Model.City;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CityDaoJDBC implements CityDao {
    private Connection connection;

    public CityDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public City findById(int id) {
        String sql = "SELECT * FROM city WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return extractCityFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<City> findByCode(String code) {
        String sql = "SELECT * FROM city WHERE countryCode = ?";
        List<City> cities = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, code);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                cities.add(extractCityFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }

    @Override
    public List<City> findByName(String name) {
        String sql = "SELECT * FROM city WHERE name = ?";
        List<City> cities = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                cities.add(extractCityFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }

    @Override
    public List<City> findAll() {
        String sql = "SELECT * FROM city";
        List<City> cities = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                cities.add(extractCityFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }

    @Override
    public City add(City city) {
        String sql = "INSERT INTO city (name, countryCode, district, population) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, city.getName());
            statement.setString(2, city.getCountryCode());
            statement.setString(3, city.getDistrict());
            statement.setInt(4, city.getPopulation());
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    city.setId(generatedKeys.getInt(1));
                }
                return city;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public City update(City city) {
        String sql = "UPDATE city SET name = ?, countryCode = ?, district = ?, population = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, city.getName());
            statement.setString(2, city.getCountryCode());
            statement.setString(3, city.getDistrict());
            statement.setInt(4, city.getPopulation());
            statement.setInt(5, city.getId());
            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                return city;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int delete(City city) {
        String sql = "DELETE FROM city WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, city.getId());
            return statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private City extractCityFromResultSet(ResultSet rs) throws SQLException {
        return new City(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("countryCode"),
                rs.getString("district"),
                rs.getInt("population")
        );
    }
}
