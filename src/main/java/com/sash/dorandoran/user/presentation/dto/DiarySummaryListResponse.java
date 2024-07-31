package com.sash.dorandoran.user.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiarySummaryListResponse {

    private List<DiarySummaryResponse> diaries;

}
