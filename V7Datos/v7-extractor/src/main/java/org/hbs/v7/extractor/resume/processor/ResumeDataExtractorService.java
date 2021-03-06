package org.hbs.v7.extractor.resume.processor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map;

import org.hbs.core.kafka.IKafkaConstants;
import org.hbs.v7.beans.model.resume.ResumeData;

public class ResumeDataExtractorService implements IKafkaConstants
{
	private static final long					serialVersionUID	= 7157231262042487975L;

	private static ResumeDataExtractorService	extractor			= null;

	public void execute(MediatorBean inBean)
	{
		try
		{
			ResumeData rData = new ResumeData();
			Map<EResumeOptions, Class<?>[]> rMap = EResumeOptions.getProcessors();
			Object object = null;
			System.out.println(">>>>>ResumeData>>STARTS>>>>>> >>>>>>>>>>> >>>>>>>>>>> >>>>>>>>> >>>>>>>>>>>>>> >>>>>>>>>>>>>");
			for (EResumeOptions eOpt : rMap.keySet())
			{
				System.out.println(eOpt.name() + " >>>> " + eOpt.method);
				for (Class<?> clazz : rMap.get(eOpt))
				{
					if (object == null || object.getClass() != clazz)
					{
						Constructor<?> constructor = clazz.getConstructor(new Class[] { MediatorBean.class });
						object = constructor.newInstance(inBean);
					}

					try
					{
						System.out.println(object.getClass().getSimpleName() + " >>>> ");
						Method method = object.getClass().getMethod(eOpt.method, new Class[] { ResumeData.class });
						method.invoke(object, rData);
					}
					catch (NoSuchMethodException excep)
					{
						System.out.printf(">>>>>>>>>>> %s method NOT available in %s class<<<<<<<<<<<<<<<<<<<<<<<<<<\n", eOpt.method, clazz.getSimpleName());
					}

				}
			}
			System.out.println(">>>>>ResumeData>>END>>>>>> >>>>>>>>>>> >>>>>>>>>>> >>>>>>>>> >>>>>>>>>>>>>> >>>>>>>>>>>>>");
			//System.out.println(">>>>>>>ResumeData>>>>>> " + new Gson().toJson(rData));
		}
		catch (Exception excep)
		{
			excep.printStackTrace();
		}
	}

	public static ResumeDataExtractorService getInstance()
	{
		if (extractor == null)
		{
			extractor = new ResumeDataExtractorService();
		}
		return extractor;
	}
}
