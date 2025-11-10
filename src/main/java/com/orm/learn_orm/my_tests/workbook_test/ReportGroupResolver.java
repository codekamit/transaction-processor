package com.orm.learn_orm.my_tests.workbook_test;

import com.orm.learn_orm.my_tests.workbook_test.custom_annotation.PartialExport;
import com.orm.learn_orm.my_tests.workbook_test.custom_annotation.PerfectExport;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ReportGroupResolver {

    private static final Map<String, Class<?>> GROUP_MAP = new HashMap<>();

    static {
        GROUP_MAP.put("Partial", PartialExport.class);
        GROUP_MAP.put("Perfect", PerfectExport.class);
    }

    /**
     * MODIFIED: Simplified to resolve a single string.
     *
     * @param groupName A string like "ClientReport"
     * @return The corresponding Class (e.g., ClientReport.class) or null if not found.
     */
    public Class<?> resolve(String groupName) {
        if (groupName == null || groupName.isEmpty()) {
            return null;
        }
        if(!GROUP_MAP.containsKey(groupName)) {
            throw new RuntimeException(groupName + " could not be resolved into a valid exportable group");
        }
        return GROUP_MAP.get(groupName);
    }
}
