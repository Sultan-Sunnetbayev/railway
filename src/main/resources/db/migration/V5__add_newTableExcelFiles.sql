CREATE TABLE "excel_files"(
    "id" SERIAL PRIMARY KEY NOT NULL ,
    "name" CHARACTER VARYING (50) UNIQUE NOT NULL ,
    "path" CHARACTER VARYING (100) NOT NULL ,
    "created" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    "updated" TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE "data"
    ADD COLUMN "excel_file_id" INT NOT NULL ,
    ADD CONSTRAINT "data_excel_file_id_fk"
        FOREIGN KEY("excel_file_id")
            REFERENCES "excel_files"("id")
                ON UPDATE CASCADE ON DELETE CASCADE ;
