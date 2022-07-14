ALTER TABLE "document"
    ADD COLUMN "user_id" INT NOT NULL ,
    ADD COLUMN "logist_name" VARCHAR (30) NOT NULL ,
    ADD COLUMN "logist_surname" VARCHAR (40) NOT NULL ,
    ADD COLUMN "status" BOOLEAN NOT NULL;