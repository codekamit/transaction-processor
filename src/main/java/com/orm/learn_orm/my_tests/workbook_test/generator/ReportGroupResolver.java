package com.orm.learn_orm.my_tests.workbook_test.generator;

import com.orm.learn_orm.my_tests.workbook_test.custom_annotation.PartialExport;
import com.orm.learn_orm.my_tests.workbook_test.custom_annotation.PerfectExport;
import com.orm.learn_orm.my_tests.workbook_test.enums.ExportFor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReportGroupResolver {

    private static final Map<ExportFor, Class<?>> GROUP_MAP = new HashMap<>();

    static {
        GROUP_MAP.put(ExportFor.PARTIAL, PartialExport.class);
        GROUP_MAP.put(ExportFor.PERFECT, PerfectExport.class);
    }

    /**
     * MODIFIED: Simplified to resolve a single string.
     *
     * @param groupName A string like "ClientReport"
     * @return The corresponding Class (e.g., ClientReport.class) or null if not found.
     */
    public Class<?> resolve(ExportFor groupName) {
        if (groupName == null) {
            return null;
        }
        if(!GROUP_MAP.containsKey(groupName)) {
            return null;
        }
        return GROUP_MAP.get(groupName);
    }
}
