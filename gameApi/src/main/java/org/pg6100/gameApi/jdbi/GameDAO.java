package org.pg6100.gameApi.jdbi;

import org.pg6100.gameApi.model.Game;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(GameMapper.class)
public interface GameDAO {

    public static final String GAME = "GAME";

    @SqlQuery("select * from " + GAME + " limit :limit")
    List<Game> getAll(@Bind("limit") Integer limit);

    @SqlQuery("select * from " + GAME + " where id = :id")
    Game findById(@Bind("id") Long id);

    @SqlUpdate("insert into " + GAME + " (questions, answeredQuestions) values (:questions, :answeredQuestions)")
    int insert(@Bind("questions") Long[] questions,
               @Bind("answeredQuestions") int answeredQuestions);
}
