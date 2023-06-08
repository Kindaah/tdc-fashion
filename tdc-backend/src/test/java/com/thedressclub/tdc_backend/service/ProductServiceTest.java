package com.thedressclub.tdc_backend.service;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.thedressclub.tdc_backend.model.Product;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.Test;

/**
 * Test class for {@link ProductService}.
 *
 * @author Gustavo Nunez.
 */
public class ProductServiceTest extends GenericServiceTest<Product>
{
    @Test
    public void testConstructor()
    throws NoSuchMethodException
    {
        Constructor<ProductService> constructor = ProductService.class.getDeclaredConstructor();
        boolean isPublic = Modifier.isPublic(constructor.getModifiers());
        ProductService productService = new ProductService();

        assertTrue("The constructor is not public: ", isPublic);
        assertNull("The productDao is not null", productService.getGenericDAO());
    }
}
