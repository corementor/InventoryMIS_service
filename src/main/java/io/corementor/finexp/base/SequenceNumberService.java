package io.corementor.finexp.base;


import io.corementor.finexp.common.ESequenceType;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;
import psychemesh.framework.common.util.EEntityLifeCycle;
import psychemesh.framework.core.message.IUserMessage;

/**
 * The Interface SequenceNumberService.
 *
 * @author Blaise Mugisha.
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
                    .orElseThrow(()-> new ObjectNotFoundException(IUserMessage.INFORMATION_NOT_FOUND , "Information Not Found"));
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
