扩展 validate 实现自定义文件校验注解(可复用)

可以进行单独拦截参数校验的异常

````java
/**
 * RequestParam 参数格式校验不通过 异常
 * @return
 */
@ExceptionHandler(ConstraintViolationException.class)
public ApiResponse handleConstraintViolationException(ConstraintViolationException ex){
		Set<ConstraintViolation<?>>constraintViolations=ex.getConstraintViolations();
		List<ConstraintViolation<?>>list=new ArrayList<>(constraintViolations);
		ConstraintViolation<?> constraintViolation=list.get(0);
		return new ApiResponse(INVALID_REQUEST.getErrorCode(),constraintViolation.getMessage());
		}

/**
 * RequestBody 参数校验不通过 异常
 * @param ex
 * @return
 */
@ExceptionHandler(MethodArgumentNotValidException.class)
public ApiResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		BindingResult bindingResult=ex.getBindingResult();
		List<FieldError> fieldErrors=bindingResult.getFieldErrors();
		return new ApiResponse(INVALID_REQUEST.getErrorCode(),fieldErrors.get(0).getDefaultMessage());
		}
````