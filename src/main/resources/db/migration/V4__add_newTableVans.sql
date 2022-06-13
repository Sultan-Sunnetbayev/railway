CREATE TABLE "vans"(
    "id" SERIAL PRIMARY KEY,
    "code" CHARACTER VARYING (20) UNIQUE NOT NULL ,
    "year_building" DOUBLE PRECISION ,
    "period_duty" DOUBLE PRECISION ,
    "end_of_the_duty" DOUBLE PRECISION ,
    "date_repear" TIMESTAMP ,
    "date_next_repear" TIMESTAMP ,
    "date_act" TIMESTAMP ,
    "period_lease" CHARACTER VARYING(50) ,
    "comment" CHARACTER VARYING (255) ,
    CHECK("year_building">0),
    CHECK("end_of_the_duty">0)
);