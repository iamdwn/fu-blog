package tech.fublog.FuBlog.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WeightRatioResponseDTO
{
    private Double amountInCurrent;
    private Double amountInPrevious;
    private Double weight;
}
