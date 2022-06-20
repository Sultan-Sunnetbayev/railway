ALTER TABLE "excel_files"
    ADD COLUMN "data_fixing_id" INT NOT NULL ,
    ADD CONSTRAINT "excel_files_data_fixing_id"
        FOREIGN KEY("data_fixing_id")
            REFERENCES "data_fixings"("id")
                ON UPDATE CASCADE ON DELETE CASCADE ;