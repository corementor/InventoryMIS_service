package io.corementor.finexp.base;


import io.corementor.finexp.inventory.common.util.ESequenceType;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import psychemesh.framework.common.util.EEntityLifeCycle;

/**
 * The Interface SequenceNumberService.
 *
 * @author Jeremie Ukundwa Tuyisenge
 * @version 1.0
 */

@Service(ISequenceNumberService.NAME)
@AllArgsConstructor
public class SequenceNumberService implements ISequenceNumberService{

    /** The sequence number repo. */
    private final ISequenceNumberRepo sequenceNumberRepo;

    /**
     * Get next sequence number
     * @param type the type
     * @return long
     */
    @Override
    public Long getNextSequenceNumber(ESequenceType type) {
        try{
            SequenceNumber theSequence = sequenceNumberRepo.findSequenceNumberByType(type)
                    .orElseThrow(()-> new ObjectNotFoundException(IMessage.INFORMATION_NOT_FOUND , "Information Not Found"));
            Long number = theSequence.getNumber();
            theSequence.setNumber(number+ 1L);
            sequenceNumberRepo.save(theSequence);
            return number;
        }catch (ObjectNotFoundException ex){
            SequenceNumber theSequence = new SequenceNumber();
            theSequence.setType(type);
            theSequence.setState(EEntityLifeCycle.ACTIVE);
            theSequence.setNumber(1L);
            sequenceNumberRepo.save(theSequence);
            return theSequence.getNumber();
        }
    }
}
