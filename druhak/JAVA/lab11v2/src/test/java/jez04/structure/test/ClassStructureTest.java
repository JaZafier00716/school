package jez04.structure.test;

import cz.vsb.fei.kelvin.unittest.StructureHelper;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import lab.score.Score;
import lab.score.ScoreException;
import lab.score.ScoreRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class ClassStructureTest {
    StructureHelper helper = StructureHelper.getInstance(ClassStructureTest.class);

    @Test
    void testGetConnection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        assertThat(clallScoreRepositoryGetConnection(), Matchers.notNullValue());
    }

    @Test
    void testGetConnection2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Connection connection = clallScoreRepositoryGetConnection();
        assertThat(connection, Matchers.notNullValue());
        assertThat(connection, Matchers.equalTo(clallScoreRepositoryGetConnection()));
    }

    private Connection clallScoreRepositoryGetConnection() throws NoSuchMethodException, InvocationTargetException,
        IllegalAccessException {
        Method getConnection = ScoreRepository.class.getDeclaredMethod("getConnection");
        getConnection.setAccessible(true);
        Object connection = getConnection.invoke(null);
        return (Connection) connection;
    }

    private Connection getScoreRepositoryConnection() throws IllegalAccessException, NoSuchFieldException {
        Field connectionFiled = ScoreRepository.class.getDeclaredField("connection");
        connectionFiled.setAccessible(true);
        Object connection = connectionFiled.get(null);
        return (Connection) connection;
    }

    @Test
    void testInit() throws NoSuchFieldException, IllegalAccessException, InvocationTargetException,
        NoSuchMethodException, SQLException {
        ScoreRepository.init();
        assertThat(getScoreRepositoryConnection(), Matchers.notNullValue());
        Connection connection = clallScoreRepositoryGetConnection();
        boolean foundTable = false;
        try(ResultSet resultSet = connection.getMetaData().getTables(null,null, null, null)){
            while(resultSet.next()){
                foundTable |= resultSet.getString(1).toLowerCase().matches("score.*");
            }
        }
        assertThat("Table 'Scores' not exist", foundTable, Matchers.is(true));
    }

    @Test
    void testInit2() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException,
        NoSuchFieldException, SQLException {
        ScoreRepository.init();
        testInit();
    }

    @Test
    void testSave() throws ScoreException, SQLException, NoSuchFieldException, IllegalAccessException {
        ScoreRepository.init();
        int originalCount = countRowsInScoresTable();
        ScoreRepository.save(Score.generate());
        assertThat(countRowsInScoresTable(), Matchers.equalTo(originalCount+1));
    }

    @Test
    void testSaveCol() throws ScoreException, SQLException, NoSuchFieldException, IllegalAccessException {
        ScoreRepository.init();
        int originalCount = countRowsInScoresTable();
        ScoreRepository.save(List.of(Score.generate(), lab.score.Score.generate()));
        assertThat(countRowsInScoresTable(), Matchers.equalTo(originalCount+2));
    }

    @Test
    void testLoad() throws ScoreException, SQLException, NoSuchFieldException, IllegalAccessException {
        ScoreRepository.init();
        int count = ScoreRepository.load().size();
        assertThat(count, Matchers.notNullValue());
        assertThat(count, Matchers.equalTo(countRowsInScoresTable()));
    }

    @Test
    void testDelete() throws ScoreException, NoSuchFieldException, IllegalAccessException, SQLException {
        ScoreRepository.init();
        deleteAll();
        assertThat(ScoreRepository.load(), Matchers.empty());
        assertThat(countRowsInScoresTable(), Matchers.is(0));
    }

    private int countRowsInScoresTable() throws SQLException, NoSuchFieldException, IllegalAccessException {
        try(Statement stm = getScoreRepositoryConnection().createStatement();
            ResultSet resultSet = stm.executeQuery("Select count(*) from Scores")){
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    @Test
    void testDeleteSaveLoad() throws ScoreException {
        ScoreRepository.init();
        deleteAll();
        ScoreRepository.save(List.of(Score.generate(), lab.score.Score.generate()));
        assertThat(ScoreRepository.load(), Matchers.hasSize(2));
    }

    @Test
    void testDeleteSaveAllLoad() throws ScoreException {
        ScoreRepository.init();
        deleteAll();
        ScoreRepository.save(Score.generate());
        assertThat(ScoreRepository.load(), Matchers.hasSize(1));
    }

    private static void deleteAll() throws ScoreException {
        for (Score score : lab.score.ScoreRepository.load()) {
            lab.score.ScoreRepository.delete(score);
        }
    }


}

