package com.mehmetsadullahguven.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {

    private ErrorMessageType errorMessageType;

    private String ofStatic;

    public String perpareErrorMessage() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.errorMessageType.getErrorMessage());
        if (this.ofStatic != null) {
                builder.append(": (" + this.ofStatic + " )");
        }
        return builder.toString();
    }

}
