package be.goodgid.objectmapper.Domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WrapperCar {

    private Car car;

    private Date datePurchased;

}
