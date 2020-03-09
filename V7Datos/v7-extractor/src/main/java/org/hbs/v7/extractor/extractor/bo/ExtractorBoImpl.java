package org.hbs.v7.extractor.extractor.bo;

import java.util.ArrayList;
import java.util.List;

import org.hbs.core.beans.model.IConfiguration;
import org.hbs.core.beans.model.channel.ConfigurationEmail;
import org.hbs.core.beans.model.channel.ConfigurationSMS;
import org.hbs.core.beans.model.channel.ConfigurationWeb;
import org.hbs.core.dao.ProducerDao;
import org.hbs.core.dao.ProducerPropertyDao;
import org.hbs.core.security.resource.IPathBase.EMedia;
import org.hbs.core.security.resource.IPathBase.EMediaMode;
import org.hbs.extractor.beans.model.resume.DataExtractorPattern;
import org.hbs.extractor.dao.IncomingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

@Component
public class ExtractorBoImpl implements ExtractorBo
{

	private static final long	serialVersionUID	= 7311996313711065555L;

	@Autowired
	ProducerDao					producerDao;
	@Autowired
	ProducerPropertyDao			producerPropertyDao;
	@Autowired
	IncomingDao					incomingDao;

	@Override
	public List<IConfiguration> getConfigurationList(EMedia eMedia, EMediaMode eMediaMode)
	{
		List<Object[]> objectList = producerPropertyDao.getProperty(eMedia, eMediaMode);

		switch ( eMedia )
		{
			case Email :
			{
				return constructConfigList(objectList, ConfigurationEmail.class);
			}
			case SMS :
			{
				return constructConfigList(objectList, ConfigurationSMS.class);
			}
			case Web :
			{
				return constructConfigList(objectList, ConfigurationWeb.class);
			}
			case WhatsApp :
			{
				return constructConfigList(objectList, ConfigurationWeb.class);
			}
			default :
				break;

		}
		return null;
	}

	private List<IConfiguration> constructConfigList(List<Object[]> objectList, Class<?> clazz)
	{
		List<IConfiguration> configList = (List<IConfiguration>) new ArrayList<IConfiguration>();

		IConfiguration config;
		Gson gson = new Gson();
		for (Object[] objectArray : objectList)
		{
			config = (IConfiguration) gson.fromJson(String.valueOf(objectArray[1]), clazz);
			config.setProducerId(String.valueOf(objectArray[0]));
			configList.add(config);
		}
		return configList;
	}

	@Override
	public long getLastEmailSentDate(String producerId)
	{
		List<Long> lastEmailSentDateList = incomingDao.getLastEmailSentDate(producerId, new PageRequest(0, 1));
		Long lastEmailSentDate = lastEmailSentDateList.size() > 0 ? lastEmailSentDateList.get(0) : 0;
		return lastEmailSentDate;// !=null &&
									// lastEmailSentDate.equals("")?Long.getLong(lastEmailSentDate):0;
	}

	@Override
	public List<DataExtractorPattern> getEmailExtractors(String producerId)
	{
		return null;
	}

}
