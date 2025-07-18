--liquibase formatted sql
--changeset ARMAL:02 splitStatements:true

ALTER TABLE form
    ADD CONSTRAINT fk_form_personal_detail
        FOREIGN KEY (fk_personal_detail_id)
            REFERENCES personal_detail (id);

ALTER TABLE form
    ADD CONSTRAINT fk_form_address
        FOREIGN KEY (fk_address_id)
            REFERENCES address (id);

ALTER TABLE address
    ADD CONSTRAINT fk_address_form
        FOREIGN KEY (fk_form_id)
            REFERENCES form (id);

ALTER TABLE personal_detail
    ADD CONSTRAINT fk_personal_detail_form
        FOREIGN KEY (fk_form_id)
            REFERENCES form (id);
