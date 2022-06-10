CREATE TABLE "name_excel_files"(
    "id" SERIAL PRIMARY KEY NOT NULL ,
    "name" CHARACTER VARYING (50) UNIQUE NOT NULL
);

ALTER TABLE "data"
    ADD COLUMN "excel_file_id" INT NOT NULL ,
    ADD CONSTRAINT "data_excel_file_id_fk"
        FOREIGN KEY("excel_file_id")
            REFERENCES "name_excel_files"("id")
                ON UPDATE CASCADE ON DELETE CASCADE ;
