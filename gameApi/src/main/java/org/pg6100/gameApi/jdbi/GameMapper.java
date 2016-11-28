package org.pg6100.gameApi.jdbi;

import org.pg6100.gameApi.model.Game;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;


import java.sql.ResultSet;
import java.sql.SQLException;

public class GameMapper implements ResultSetMapper<Game> {

    @Override
    public Game map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Game(resultSet.getLong("id"), (Long[])resultSet.getArray("questions").getArray(),
                resultSet.getInt("answeredQuestions"));
    }
}
