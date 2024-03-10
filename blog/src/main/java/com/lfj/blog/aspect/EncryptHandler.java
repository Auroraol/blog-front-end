package com.lfj.blog.aspect;

import com.lfj.blog.annotation.EncryptField;
import com.lfj.blog.annotation.EncryptMethod;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;

import static com.lfj.blog.enums.EncryptConstant.DECRYPT;
import static com.lfj.blog.enums.EncryptConstant.ENCRYPT;


@Slf4j
@Aspect
@Component
public class EncryptHandler {
	@Autowired
	private StringEncryptor stringEncryptor;

	@Pointcut("@annotation(com.lfj.blog.annotation.EncryptMethod)")
	public void pointCut() {
	}

	//在切面中实现加密和解密的功能，并确保在调用目标方法之前和之后都执行这些操作
	@Around("@annotation(pointCut)")
	public Object around(ProceedingJoinPoint joinPoint, EncryptMethod pointCut) throws Throwable {
		// 获取 @EncryptMethod 中的 type 参数值
		String type = pointCut.type();
		switch (type) {
			case ENCRYPT:
				// 执行加密操作
				return encrypt(joinPoint);
			case DECRYPT:
				// 执行解密操作
				return decrypt(joinPoint);  //环绕方法中返回值: 目标方法的执行结果
			default:
				// 如果类型不匹配，则返回 null 或其他适当的值
				return null;
		}
	}

	public Object encrypt(ProceedingJoinPoint joinPoint) throws Throwable {
		// 获取目标方法的参数
		Object[] args = joinPoint.getArgs();
		try {
			// 如果方法有参数
			if (args.length != 0) {
				for (int i = 0; i < args.length; i++) {
					Object o = args[i];
					// 如果参数是字符串类型，则对其进行加密
					if (o instanceof String) {
						args[i] = encryptValue(o);
					} else {
						// 如果参数不是字符串类型，则调用 handler 方法对其进行加密处理
						args[i] = handler(o, ENCRYPT);
					}
					//TODO 其余类型自己看实际情况加
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		// 调用目标方法，并传入修改后的参数
		// 这样方法中就能直接获取方法的参数加密后的值,
		// 不这样做方法内部方法的参数不会加密,只有返回值是加密后的
		Object result = joinPoint.proceed(args);
		return result;
	}

//	public Object decrypt(ProceedingJoinPoint joinPoint,Object[] args) {
//		Object result = null;
//		try {
//			Object obj = joinPoint.proceed(args);
//			if (obj != null) {
//				if (obj instanceof String) {
//					result = decryptValue(obj);
//				} else {
//					result = handler(obj, EncryptConstant.DECRYPT);
//				}
//				//TODO 其余类型自己看实际情况加
//			}
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//		return result;
//	}

	public Object decrypt(ProceedingJoinPoint joinPoint) {
		Object result = null;
		try {
			// 执行被切入的方法，并获取方法返回值
			Object obj = joinPoint.proceed();
			// 检查返回值是否为空
			if (obj != null) {
				// 如果返回值是字符串类型
				if (obj instanceof String) {
					// 解密字符串类型的返回值
					result = decryptValue(obj);
				} else {
					// 如果返回值不是字符串类型，调用handler方法处理返回值
					result = handler(obj, DECRYPT);
				}
				//TODO 其余类型自己看实际情况加
			}
		} catch (Throwable e) {
			// 捕获异常并打印堆栈信息
			e.printStackTrace();
		}
		// 返回解密后的结果
		return result;
	}


	private Object handler(Object obj, String type) throws IllegalAccessException {

		if (Objects.isNull(obj)) {
			return null;
		}
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			boolean annotationPresent = field.isAnnotationPresent(EncryptField.class);
			if (annotationPresent) {
				field.setAccessible(true);
				String value;
				String realValue = (String) field.get(obj);
				if (DECRYPT.equals(type)) {
					value = stringEncryptor.decrypt(realValue);
				} else {
					value = stringEncryptor.encrypt(realValue);
				}
				field.set(obj, value);
			}
		}
		return obj;
	}

	public String encryptValue(Object realValue) {
		String value = null;
		try {
			value = stringEncryptor.encrypt(String.valueOf(realValue));
		} catch (Exception ex) {
			return value;
		}
		return value;
	}

	public String decryptValue(Object realValue) {
		String value = String.valueOf(realValue);
		try {
			value = stringEncryptor.decrypt(value);
		} catch (Exception ex) {
			return value;
		}
		return value;
	}
}
