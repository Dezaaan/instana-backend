package addressbook;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import addressbook.configuration.AddressBookConfiguration;
import addressbook.resource.ContactResource;
import addressbook.service.FirebaseService;
import addressbook.serviceI.FirebaseServiceI;
import io.dropwizard.Application;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AddressBookAplication extends Application<AddressBookConfiguration> {

	public static void main(String[] args) throws Exception {
		new AddressBookAplication().run("server", "configuration.yml");
	}

	@Override
	public void run(AddressBookConfiguration configuration, Environment environment) throws Exception {
		final FilterRegistration.Dynamic cors = environment.servlets().addFilter("CORS", CrossOriginFilter.class);

		cors.setInitParameter("allowedOrigins", "*");
		cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin, ");
		cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
		cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
		
		FirebaseOptions options = configuration.getFirebaseOptionsFactory().build();
		FirebaseApp.initializeApp(options);

		FirebaseServiceI service = new FirebaseService();
		ContactResource resource = new ContactResource(service);
		environment.jersey().register(resource);
	}

	@Override
	public void initialize(Bootstrap<AddressBookConfiguration> bootstrap) {
		bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
		bootstrap.addBundle(new MultiPartBundle());
		super.initialize(bootstrap);
	}
}
