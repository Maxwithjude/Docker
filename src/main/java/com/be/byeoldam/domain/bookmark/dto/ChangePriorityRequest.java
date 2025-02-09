package com.be.byeoldam.domain.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePriorityRequest {
    private boolean priority;

    public boolean getPriority() {
        return priority;
    }
}
