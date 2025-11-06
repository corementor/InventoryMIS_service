package io.corementor.finexp.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * The  class Request dto
 *
 * @author Blaise Mugisha.
 * @version 1.0
 */

@Getter
@Setter
public class RequestDto  {
    private UUID id;
    private String comment;
}
