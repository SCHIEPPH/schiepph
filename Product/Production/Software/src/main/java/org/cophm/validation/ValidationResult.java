package org.cophm.validation;

/**
 * Created by
 * User: ralph
 * Date: 4/10/12
 * Time: 5:02 PM
 */
public class ValidationResult {

    private String            errorCode;
    private String            errorMessage;
    private String            fieldName;
    private ErrorSeverity     severity;

    public ValidationResult() {
    }

    public ValidationResult(String errorCode, String  errorMessage, String fieldName, ErrorSeverity severity) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.fieldName = fieldName;
        this.severity = severity;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof ValidationResult)) return false;

        ValidationResult that = (ValidationResult)o;

        if(errorCode != null ? !errorCode.equals(that.errorCode) : that.errorCode != null) return false;
        if(errorMessage != null ? !errorMessage.equals(that.errorMessage) : that.errorMessage != null) return false;
        if(fieldName != null ? !fieldName.equals(that.fieldName) : that.fieldName != null) return false;
        if(severity != that.severity) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = errorCode != null ? errorCode.hashCode() : 0;
        result = 31 * result + (errorMessage != null ? errorMessage.hashCode() : 0);
        result = 31 * result + (fieldName != null ? fieldName.hashCode() : 0);
        result = 31 * result + (severity != null ? severity.hashCode() : 0);
        return result;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public ErrorSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(ErrorSeverity severity) {
        this.severity = severity;
    }
}
