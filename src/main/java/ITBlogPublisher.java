import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


public class ITBlogPublisher {
    private static final String EXCHANGE_NAME = "NEWS-CHANNEL";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("123456");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            System.out.println("Welcome to command line 'IT BLOG ' ");
            System.out.println("Enter your news. Example - php * some message");
            Scanner in = new Scanner(System.in);
            String topic;
            do {
                String inputCommand = in.nextLine();
                String[] separateInput = inputCommand.split(" ");
                topic = separateInput[0];
                StringBuilder outMessage= new StringBuilder();
                for (int i = 2; i < separateInput.length; i++) {
                  outMessage.append(separateInput[i]).append(" ");
                }
                channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
                channel.basicPublish(EXCHANGE_NAME, topic, null, outMessage.toString().getBytes(StandardCharsets.UTF_8));
                System.out.println(" [x] Sent message channel - " + topic + " message - " + outMessage);
            } while (!topic.equals("exit"));


        }
    }
}
