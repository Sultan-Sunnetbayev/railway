CREATE TABLE "data_fixings"(
    "id" SERIAL PRIMARY KEY NOT NULL ,
    "name" CHARACTER VARYING (50) NOT NULL ,
    "created" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    "updated" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO "data_fixings"("name") VALUES ('hazar_logistika'),
                                          ('A logist'),
                                          ('B logist'),
                                          ('C logist'),
                                          ('D logist');
