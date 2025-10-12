package com.orm.learn_orm.file_management.header;

import com.orm.learn_orm.exception.UnsupportedFileException;
import io.micrometer.common.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IFileHeader {

    Logger LOGGER = LogManager.getLogger(IFileHeader.class);

    String FOOTER_INDEX_ERR_MSG = "Footer column index is out of bounds.";

    int headerStartRow();
    int headerStartCol();
    int footerCol();
    int sheetNumber();
    String footerText();
    List<String> headers();

    default boolean allHeadersPresent(List<String> headers, IFileHeader fileHeader) {
        Set<String> headerSet = Set.copyOf(headers);
        for(int idx = 0; idx < fileHeader.headers().size(); idx++) {
            String header = fileHeader.headers().get(idx);
            if(!headerSet.contains(header)) {
                return false;
            }
        }

        return true;
    }

    default Map<String, Integer> getHeaderIndices(List<String> headers) {
        Map<String, Integer> headerIndices = new HashMap<>();
        for(int idx = 0; idx < headers.size(); idx++) {
            headerIndices.put(headers.get(idx), idx);
        }
        return headerIndices;
    }

    default boolean hasReachedFooter(List<String> currentRow, IFileHeader header) {
        try {
            if(currentRow.get(header.footerCol()) == null) {
                return false;
            }
        }
        catch(IndexOutOfBoundsException ex) {
            LOGGER.warn("Exception in hasReachedFooter method: "+ex.getMessage());
            throw new UnsupportedFileException(FOOTER_INDEX_ERR_MSG);
        }
        return currentRow.get(header.footerCol()).trim().startsWith(header.footerText());
    }

    default boolean isRowEmpty(List<String> currentRow){
        for (String s : currentRow) {
            if (!StringUtils.isBlank(s)) {
                return false;
            }
        }
        return true;
    }
}
