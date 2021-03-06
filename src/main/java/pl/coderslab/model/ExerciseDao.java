package pl.coderslab.model;

import pl.coderslab.utils.DBUtil;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExerciseDao {

    public static final ExerciseDao dao = new ExerciseDao();

    private static final String CREATE_EXERCISE_QUERY =
            "INSERT INTO exercises(title, description) VALUES (?, ?)";
    private static final String READ_EXERCISE_QUERY =
            "SELECT * FROM exercises where id = ?";
    private static final String UPDATE_EXERCISE_QUERY =
            "UPDATE exercises SET title = ?, description = ? where id = ?";
    private static final String DELETE_EXERCISE_QUERY =
            "DELETE FROM exercises WHERE id = ?";
    private static final String FIND_ALL_EXERCISES_QUERY =
            "SELECT * FROM exercises";

    public Exercise createExercise(Exercise exercise) {
        try (Connection connection = DBUtil.connect()) {
            PreparedStatement preS = connection.prepareStatement(CREATE_EXERCISE_QUERY, PreparedStatement.RETURN_GENERATED_KEYS);
            preS.setString(1, exercise.getTitle());
            preS.setString(2, exercise.getDescription());
            preS.executeUpdate();
            ResultSet rs = preS.getGeneratedKeys();
            if (rs.next()) {
                exercise.setId(rs.getInt(1));
            }
            return exercise;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Exercise readExercise(int exerciseId) {
        try (Connection connection = DBUtil.connect()) {
            PreparedStatement preS = connection.prepareStatement(READ_EXERCISE_QUERY);
            preS.setInt(1, exerciseId);
            ResultSet rs = preS.executeQuery();
            if (rs.next()) {
                Exercise exercise = new Exercise();
                exercise.setId(rs.getInt("id"));
                exercise.setTitle(rs.getString("title"));
                exercise.setDescription(rs.getString("description"));
                return exercise;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateExercise(Exercise exercise) {
        try (Connection connection = DBUtil.connect()) {
            PreparedStatement preS = connection.prepareStatement(UPDATE_EXERCISE_QUERY);
            preS.setString(1, exercise.getTitle());
            preS.setString(2, exercise.getDescription());
            preS.setInt(3, exercise.getId());
            preS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteExercise(int exerciseId) {
        try (Connection connection = DBUtil.connect()) {
            PreparedStatement preS = connection.prepareStatement(DELETE_EXERCISE_QUERY);
            preS.setInt(1, exerciseId);
            preS.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e){
            JOptionPane.showMessageDialog(null, "Delete all usages of this exercise\n before deleting this exercise", "Exercise in use", JOptionPane.WARNING_MESSAGE);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Exercise> findAllExercises() {
        try (Connection connection = DBUtil.connect()) {
            PreparedStatement preS = connection.prepareStatement(FIND_ALL_EXERCISES_QUERY);
            ResultSet rs = preS.executeQuery();
            List<Exercise> allExercises = new ArrayList<>();
            while (rs.next()) {
                Exercise tmp = new Exercise();
                tmp.setId(rs.getInt("id"));
                tmp.setTitle(rs.getString("title"));
                tmp.setDescription(rs.getString("description"));
                int usagesCount = SolutionDao.dao.findAllByExerciseId(tmp.getId()).size();
                tmp.setUsagesCount(usagesCount);
                allExercises.add(tmp);
            }
            return allExercises;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Exercise[] addExerciseToArray(Exercise exercise, Exercise[] allExercises) {
        Exercise[] result = Arrays.copyOf(allExercises, allExercises.length + 1);
        result[allExercises.length] = exercise;
        return result;
    }
}
