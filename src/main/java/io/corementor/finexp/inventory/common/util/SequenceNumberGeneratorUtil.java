package io.corementor.finexp.inventory.common.util;

import io.corementor.finexp.inventory.base.ISequenceNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The class SequenceNumberGeneratorUtil.
 *
 * @author Blaise Mugisha
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class SequenceNumberGeneratorUtil {

    /** The sequence number service. */
    private final ISequenceNumberService sequenceNumberService;

    /**
     * Number identifier generator
     *
     * @param type the type
     * @param prefix the prefix
     * @return string
     */
    public String getIdentifier(ESequenceType type , ESequencePrefix prefix){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDateTime = now.format(dateTimeFormatter);
        long nextSequenceNumber = sequenceNumberService.getNextSequenceNumber(type);
        return String.format("%s%s-%03d",prefix, formattedDateTime, nextSequenceNumber);
    }
}
