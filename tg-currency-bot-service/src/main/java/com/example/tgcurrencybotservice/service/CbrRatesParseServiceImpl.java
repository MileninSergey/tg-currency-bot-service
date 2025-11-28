package com.example.tgcurrencybotservice.service;

import com.example.tgcurrencybotservice.client.CbrClient;
import com.example.tgcurrencybotservice.exceptions.ServiceException;
import com.example.tgcurrencybotservice.repository.CurrencyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

@Service
public class CbrRatesParseServiceImpl implements CbrRatesParseService{
    private static final Logger LOG = LoggerFactory.getLogger(CbrRatesParseServiceImpl.class);
    //переменная с путем до USD в XML - это правила, по которым выполняется поиск нужных данных в XML.
    private static final String USD_XPATH = "/ValCurs//Valute[@ID='R01235']/Value";

    @Autowired
    private CbrClient cbrClient;

    public String getUSD() throws ServiceException {
        LOG.info("Calling getUSD()...");
        var xmlOptional = cbrClient.getCurrencyRatesXML();
        String bodyXml = xmlOptional.orElseThrow(
                () -> new ServiceException("Не удалось получить XML")
        );
        String value = extractCurrencyValueFromXML(bodyXml, USD_XPATH);
        LOG.info("Calling getUSD()..."+ value);
        return value;
    }

    private String extractCurrencyValueFromXML(String xml, String xpathExpression) throws ServiceException {
        var source = new InputSource(new StringReader(xml));
        try {
            //создание XPath процессора для работы с XML документами
            var xpath = XPathFactory.newInstance().newXPath();
            var document = (Document) xpath.evaluate("/", source, XPathConstants.NODE);

            return xpath.evaluate(xpathExpression, document);
        } catch (XPathExpressionException e) {
            throw new ServiceException("Не удалось обработать XML", e);
        }
    }
}
