package com.demo.mcp_server.tool.patient;

import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.stereotype.Component;

/**
 * @author Pramod Rajane
 */

@Component
public class PatientTools {

    @McpTool(name = "patientInfo", description = "This tool will return information about patients, " +
            "id is unique identifier used across other tools.")
    public String patientInfo() {
        return "{\n" +
                "  \"totalPatients\": 2,\n" +
                "  \"patients\": [\n" +
                "    {\n" +
                "      \"resourceType\": \"Patient\",\n" +
                "      \"id\": \"01\",\n" +
                "      \"identifier\": \"MRN-998231\",\n" +
                "      \"name\": {\n" +
                "        \"given\": \"Aarav\",\n" +
                "        \"family\": \"Sharma\"\n" +
                "      },\n" +
                "      \"gender\": \"male\",\n" +
                "      \"birthDate\": \"1985-06-14\",\n" +
                "      \"telecom\": \"+919876543210\",\n" +
                "      \"address\": {\n" +
                "        \"city\": \"Mumbai\",\n" +
                "        \"country\": \"India\"\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"resourceType\": \"Patient\",\n" +
                "      \"id\": \"02\",\n" +
                "      \"identifier\": \"MRN-445129\",\n" +
                "      \"name\": {\n" +
                "        \"given\": \"Priya\",\n" +
                "        \"family\": \"Patel\"\n" +
                "      },\n" +
                "      \"gender\": \"female\",\n" +
                "      \"birthDate\": \"1990-11-22\",\n" +
                "      \"telecom\": \"+919123456789\",\n" +
                "      \"address\": {\n" +
                "        \"city\": \"Pune\",\n" +
                "        \"country\": \"India\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}\n";
    }

    @McpTool(name = "patientHistoryInfo", description = "This tool will return the medical history of a patient, " +
            "id is unique identifier used across other tools.")
    public String patientHistoryInfo() {
        return "[\n" +
                "{\n" +
                "  \"id\": \"01\",\n" +
                "  \"diagnosis_history\": [\n" +
                "    {\n" +
                "      \"diagnosis_id\": \"DX-5021\",\n" +
                "      \"date\": \"2024-01-15\",\n" +
                "      \"condition\": \"Essential Hypertension\",\n" +
                "      \"icd_code\": \"I10\",\n" +
                "      \"status\": \"Active\",\n" +
                "      \"doctor\": \"Dr. Robert Smith\",\n" +
                "      \"notes\": \"Managed with diet and lisinopril.\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"diagnosis_id\": \"DX-4890\",\n" +
                "      \"date\": \"2022-08-20\",\n" +
                "      \"condition\": \"Acute Bronchitis\",\n" +
                "      \"icd_code\": \"J20.9\",\n" +
                "      \"status\": \"Resolved\",\n" +
                "      \"doctor\": \"Dr. John Green\",\n" +
                "      \"notes\": \"Treated with a short course of antibiotics and rest.\"\n" +
                "    }\n" +
                "  ]\n" +
                "},\n" +
                "{\n" +
                "  \"id\": \"02\",\n" +
                "  \"diagnosis_history\": [\n" +
                "    {\n" +
                "      \"diagnosis_id\": \"DX-5021\",\n" +
                "      \"date\": \"2024-01-15\",\n" +
                "      \"condition\": \"Coronary Artery Blockage\",\n" +
                "      \"icd_code\": \"I10\",\n" +
                "      \"status\": \"Active\",\n" +
                "      \"doctor\": \"Dr. Albert Smith\",\n" +
                "      \"notes\": \"Managed with diet and lisinopril.\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"diagnosis_id\": \"DX-4890\",\n" +
                "      \"date\": \"2022-08-20\",\n" +
                "      \"condition\": \"Asthama\",\n" +
                "      \"icd_code\": \"J20.9\",\n" +
                "      \"status\": \"Resolved\",\n" +
                "      \"doctor\": \"Dr. Alice Green\",\n" +
                "      \"notes\": \"Treated with a short course of antibiotics and rest.\"\n" +
                "    }\n" +
                "  ]\n" +
                "}]";
    }

    @McpTool(name = "treatmentInfo", description = "This tool will return the treatment information of a patient, " +
            "id is unique identifier used across other tools.")
    public String treatmentInfo() {
        return "[\n" +
                "  {\n" +
                "    \"treatment_id\": \"TRX-98421\",\n" +
                "    \"id\": \"01\",\n" +
                "    \"diagnosis\": \"Acute Bronchitis\",\n" +
                "    \"prescribing_doctor\": {\n" +
                "      \"id\": \"DOC-441\",\n" +
                "      \"name\": \"Dr. Robert Smith\"\n" +
                "    },\n" +
                "    \"start_date\": \"2026-06-01\",\n" +
                "    \"end_date\": \"2026-06-14\",\n" +
                "    \"status\": \"Active\",\n" +
                "    \"treatment_plan\": [\n" +
                "      {\n" +
                "        \"step_number\": 1,\n" +
                "        \"type\": \"Medication\",\n" +
                "        \"name\": \"Amoxicillin\",\n" +
                "        \"dosage\": \"500mg\",\n" +
                "        \"frequency\": \"Three times a day\",\n" +
                "        \"duration_days\": 7\n" +
                "      },\n" +
                "      {\n" +
                "        \"step_number\": 2,\n" +
                "        \"type\": \"Medication\",\n" +
                "        \"name\": \"Ibuprofen\",\n" +
                "        \"dosage\": \"400mg\",\n" +
                "        \"frequency\": \"As needed for pain/fever\",\n" +
                "        \"duration_days\": 5\n" +
                "      }\n" +
                "    ],\n" +
                "    \"follow_up_required\": true,\n" +
                "    \"follow_up_date\": \"2026-06-15\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"treatment_id\": \"TRX-98421\",\n" +
                "    \"id\": \"02\",\n" +
                "    \"diagnosis\": \"Acute Bronchitis\",\n" +
                "    \"prescribing_doctor\": {\n" +
                "      \"id\": \"DOC-441\",\n" +
                "      \"name\": \"Dr. Robert Smith\"\n" +
                "    },\n" +
                "    \"start_date\": \"2026-06-01\",\n" +
                "    \"end_date\": \"2026-06-14\",\n" +
                "    \"status\": \"Active\",\n" +
                "    \"treatment_plan\": [\n" +
                "      {\n" +
                "        \"step_number\": 1,\n" +
                "        \"type\": \"Oxygen\",\n" +
                "        \"name\": \"Oxide\",\n" +
                "        \"dosage\": \"1500mg\",\n" +
                "        \"frequency\": \"Three times a day\",\n" +
                "        \"duration_days\": 7\n" +
                "      }\n" +
                "    ],\n" +
                "    \"follow_up_required\": true,\n" +
                "    \"follow_up_date\": \"2026-06-15\"\n" +
                "  }\n" +
                "]";
    }
}
