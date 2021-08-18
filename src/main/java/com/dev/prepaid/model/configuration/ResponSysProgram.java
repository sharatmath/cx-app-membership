package com.dev.prepaid.model.configuration;

import lombok.Data;

@Data
public class ResponSysProgram {

    public ResponSysProgram(String programId, String programName){
        this.programId = programId;
        this.programName = programName;
    }

    String programId;
    String programName;
}
