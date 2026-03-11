module cs.vsb.cz.fei.java2.db {
    exports cs.vsb.cz.fei.java2.db.score;
    requires static lombok;
    requires org.apache.logging.log4j;
    requires java.sql;
    requires com.h2database;
    requires cs.vsb.cz.fei.java2.api;

    provides cs.vsb.cz.fei.java2.api.score.ScoreStorageInterface with cs.vsb.cz.fei.java2.db.score.ScoreRepository;
}