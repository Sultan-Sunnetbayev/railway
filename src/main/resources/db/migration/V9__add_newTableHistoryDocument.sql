CREATE TABLE "document"(
    "id" SERIAL PRIMARY KEY NOT NULL ,
    "name" CHARACTER VARYING (50) NOT NULL ,
    "import" CHARACTER VARYING (10) ,
    "export" CHARACTER VARYING (10) ,
    "instruction" CHARACTER  VARYING (100) ,
    "dispatch" CHARACTER VARYING (100) ,
    "created" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    "updated" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)