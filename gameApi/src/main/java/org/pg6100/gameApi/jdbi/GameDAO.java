package org.pg6100.gameApi.jdbi;

import org.pg6100.gameApi.model.Game;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(GameMapper.class)
public interface GameDAO {

    String GAME_TABLE = "GAME_TABLE";

    @SqlUpdate("CREATE TABLE " + GAME_TABLE +
            " (id BIGINT AUTO_INCREMENT PRIMARY KEY," +
            " questions ARRAY," +
            " answeredQuestions INT)")
    void createGameTable();

    @SqlQuery("select * from " + GAME_TABLE + " limit :limit")
    List<Game> getAll(@Bind("limit") Integer limit);

    @SqlQuery("select * from " + GAME_TABLE + " where id = :id")
    Game findById(@Bind("id") Long id);

    @SqlUpdate("insert into " + GAME_TABLE + " (questions, answeredQuestions) values (:questions, :answeredQuestions)")
    @GetGeneratedKeys
    Long insert(@Bind("questions") Long[] questions,
               @Bind("answeredQuestions") int answeredQuestions);
}
