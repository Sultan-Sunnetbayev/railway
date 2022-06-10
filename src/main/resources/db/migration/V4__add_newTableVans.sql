CREATE TABLE "vans"(
    "id" SERIAL PRIMARY KEY,
    "code" CHARACTER VARYING (20) UNIQUE NOT NULL ,
    "year_building" INTEGER ,
    "period_duty" DOUBLE PRECISION ,
    "end_of_the_duty" DOUBLE PRECISION ,
    "date_repear" TIMESTAMP ,
    "date_next_repear" TIMESTAMP ,
    "date_act" TIMESTAMP ,
    "period_lease" CHARACTER VARYING(30) ,
    "comment" CHARACTER VARYING (100) ,
    CHECK("year_building">0),
    CHECK("end_of_the_duty">0)
);