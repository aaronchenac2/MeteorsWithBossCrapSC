package pckg;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Server implements ActionListener
{
    static final int WIDTH = 1680;

    static final int HEIGHT = 1050;

    private static final int AVATARSIZE = 20;

    static final int MAXMET = WIDTH / 50 + 1; //WIDTH * HEIGHT / 32 / AVATARSIZE / AVATARSIZE;

    private static final int PORT = 7778;

    private ServerSocket serverSocket = null;

    private Socket socket;

    private Socket socket2;

    private PrintStream output;

    private PrintStream output2;

    private Scanner input;

    private Scanner input2;

    private ServerThread t1;

    private ServerThread t2;

    private double[][] meteorData = new double[MAXMET][6];


    public static void main( String args[] ) throws IOException, InterruptedException
    {
        Server s = new Server();
        s.startServer();
    }


    public void startServer() throws IOException, InterruptedException
    {
        for ( int j = 0; j < MAXMET; j++ )
        {
            double angle = Math.random() * 360;
            double x = Math.random() * WIDTH;
            double y = Math.random() * HEIGHT;
            meteorData[j][0] = j;
            meteorData[j][1] = angle;
            meteorData[j][2] = x;
            meteorData[j][3] = y;
            meteorData[j][4] = 0;
            meteorData[j][5] = 0;
        }
        setServer();
        getConnection();
        setThreads();
        whilePlaying();
    }


    public void setServer() throws IOException
    {
        System.out.println( "Setting Server" );
        serverSocket = new ServerSocket( PORT );
        System.out.println( "Server Set" );
    }


    public void getConnection() throws IOException
    {
        System.out.println( "Waiting for client connection..." );
        socket = serverSocket.accept();
        System.out.println( "Client 1 has connected!" );
        setStream1();
        output.println( "knight.png" );
        output.flush();
        int count = 0;
        String message;
        do
        {
            message = input.nextLine();
            if ( message.equals( "initMeteors" ) )
            {
                double angle = meteorData[count][1];
                double x = meteorData[count][2];
                double y = meteorData[count][3];

                output.println( "a" + angle ); // Angle
                output.flush();
                output.println( "x" + x ); // X
                output.flush();
                output.println( "y" + y ); // Y
                output.flush();
                count++;
            }
            else
            {
                break;
            }
        } while ( true );
        System.out.println( "Finished initializing client1 meteors!" );

        System.out.println( "Now waiting on Client 2..." );
        socket2 = serverSocket.accept();
        System.out.println( "Client 2 had connected!" );
        setStream2();
        output2.println( "knight2.png" );
        for ( int j = 0; j < meteorData.length; j++ )
        {
            for ( int i = 1; i < 4; i++ )
            {
                output2.println( meteorData[j][i] );
                output2.flush();
            }
        }
        output2.println( "finished" );
        output2.flush();
        System.out.println( "Finished initializing client2 meteors!" );

    }


    public void setStream1() throws IOException
    {
        System.out.println( "Creating stream1..." );
        output = new PrintStream( socket.getOutputStream() );
        output.flush();
        input = new Scanner( socket.getInputStream() );
        System.out.println( "Stream1 has been created!" );
    }


    public void setStream2() throws IOException
    {
        System.out.println( "Creating stream2..." );
        output2 = new PrintStream( socket2.getOutputStream() );
        output2.flush();
        input2 = new Scanner( socket2.getInputStream() );
        System.out.println( "Stream2 has been created!" );
    }


    public void setThreads()
    {
        t1 = new ServerThread( "t1" );
        t2 = new ServerThread( "t2" );
    }


    public void whilePlaying() throws InterruptedException
    {
        output.println( "go" );
        System.out.println( "Game started!" );
        t1.start();
        t2.start();
    }


    public class ServerThread extends Thread
    {
        String name;


        public ServerThread( String n )
        {
            name = n;
        }


        public synchronized void run()
        {
            if ( name.equals( "t1" ) )
            {
                while ( true )
                {
                    String message = input.nextLine();
                    if ( message.length() > 0 && ( message.substring( 0, 1 ).equals( "x" )
                        || message.substring( 0, 1 ).equals( "y" ) ) )
                    {
                        output2.println( message );
                    }
                    else if ( message.equals( "meteor" ) )
                    {
                        output2.println( message );
                        output2.flush();
                        output2.println( "v" + Math.random() * 360 ); // Velocity
                        output2.flush();
                        output2.println( "x" + Math.random() * WIDTH ); // X
                        output2.flush();
                        output2.println( "y" + Math.random() * HEIGHT ); // Y
                    }
                    else if ( message.equals( "pause" ) || message.equals( "unpause" ) )
                    {
                        output2.println( message );
                    }
                    else if ( message.equals( "tookTheL" ) )
                    {
                        output2.println( message );
                    }
                    else if ( message.equals( "restart" ) )
                    {
                        output2.println( message );
                    }
                    else if ( message.equals( "hitMet" ) )
                    {
                        output2.println( message );
                    }
                    else if ( message.equals( "revive" ) )
                    {
                        output2.println( message );
                        output2.println( input.nextInt() ); // Amount of HP
                                                            // Gained/Loss
                    }
                    else if ( message.equals( "gain" ) )
                    {
                        output2.println( message );
                    }
                    else if ( message.equals( "metUp" ) )
                    {
                        output2.println( message );
                        output2.println( input.nextInt() );
                    }
                    else if ( message.equals( "levelUp" ) )
                    {
                        output2.println( message );
                    }
                    else if ( message.equals( "hpCountUp" ) )
                    {
                        output2.println( message );
                    }
                    else if ( message.equals( "updateMeteor" ) )
                    {
                        output2.println( message );
                        output2.flush();
                        output2.println( input.nextInt() );
                        output2.flush();
                        output2.println( input.nextDouble() );
                        output2.flush();
                        output2.println( input.nextDouble() );
                        output2.flush();
                        output2.println( input.nextBoolean() );
                    }
                    else if ( message.equals( "updatePackage" ) )
                    {
                        output2.println( message );
                        output2.flush();
                        output2.println( input.nextLine() );
                        output2.flush();
                        output2.println( input.nextDouble() );
                        output2.flush();
                        output2.println( input.nextDouble() );
                        output2.flush();
                        output2.println( input.nextInt() );
                    }
                    else if ( message.equals( "updateBullet" ) )
                    {
                        output2.println( message );
                        output2.flush();
                        output2.println( input.nextInt() );
                        output2.flush();
                        output2.println( input.nextDouble() );
                        output2.flush();
                        output2.println( input.nextDouble() );
                        output2.flush();
                        output2.println( input.nextBoolean() );
                    }
                    else if (message.equals( "updateBossHP" ))
                    {
                        output2.println(message);
                        output2.flush();
                        output2.println(input.nextInt());
                    }
                    else if (message.equals( "updateBB" ))
                    {
                        output2.println(message);
                        output2.flush();
                        output2.println(input.nextInt());
                        output2.flush();
                        output2.println(input.nextDouble());
                        output2.flush();
                        output2.println(input.nextDouble());
                        output2.flush();
                        output2.println(input.nextBoolean());
                    }
                    else if ( message.equals( "quit" ) )
                    {
                        output2.println( message );
                        try
                        {
                            input.close();
                            output2.close();
                            socket.close();
                            serverSocket.close();
                        }
                        catch ( IOException e )
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    output2.flush();
                }

            }
            else if ( name.equals( "t2" ) )
            {
                while ( true )
                {
                    String message = input2.nextLine();
                    if ( message.length() > 0 && ( message.substring( 0, 1 ).equals( "x" )
                        || message.substring( 0, 1 ).equals( "y" ) ) )
                    {
                        output.println( message );
                    }
                    else if ( message.equals( "pause" ) || message.equals( "unpause" ) )
                    {
                        output.println( message );
                    }
                    else if ( message.equals( "tookTheL" ) )
                    {
                        output.println( message );
                    }
                    else if ( message.equals( "restart" ) )
                    {
                        output.println( message );
                    }
                    else if ( message.equals( "hitMet" ) )
                    {
                        output.println( message );
                    }
                    else if ( message.equals( "revive" ) )
                    {
                        output.println( message );
                        output.println( input2.nextInt() ); // Amount of HP
                                                            // Gained/Loss
                    }
                    else if ( message.equals( "gain" ) )
                    {
                        output.println( message );
                    }
                    else if ( message.equals( "metUp" ) )
                    {
                        output.println( message );
                        output.println( input2.nextInt() ); // Number of extra
                                                            // meteors
                    }
                    else if ( message.equals( "levelUp" ) )
                    {
                        output.println( message );
                    }
                    else if ( message.equals( "hpCountUp" ) )
                    {
                        output.println( message );
                    }
                    else if ( message.equals( "updateMeteor" ) )
                    {
                        output.println( message );
                        output.flush();
                        output.println( input2.nextInt() );
                        output.flush();
                        output.println( input2.nextDouble() );
                        output.flush();
                        output.println( input2.nextDouble() );
                        output.flush();
                        output.println( input2.nextBoolean() );
                    }
                    else if ( message.equals( "updatePackage" ) )
                    {
                        output.println( message );
                        output.flush();
                        output.println( input2.nextLine() );
                        output.flush();
                        output.println( input2.nextDouble() );
                        output.flush();
                        output.println( input2.nextDouble() );
                        output.flush();
                        output.println( input2.nextInt() );
                    }
                    else if ( message.equals( "updateBullet" ) )
                    {
                        output.println( message );
                        output.flush();
                        output.println( input2.nextInt() );
                        output.flush();
                        output.println( input2.nextDouble() );
                        output.flush();
                        output.println( input2.nextDouble() );
                        output.flush();
                        output.println( input2.nextBoolean() );
                    }
                    else if (message.equals( "updateBossHP" ))
                    {
                        output.println(message);
                        output.flush();
                        output.println(input2.nextInt());
                    }
                    else if (message.equals( "updateBB" ))
                    {
                        output.println(message);
                        output.flush();
                        output.println(input2.nextInt());
                        output.flush();
                        output.println(input2.nextDouble());
                        output.flush();
                        output.println(input2.nextDouble());
                        output.flush();
                        output.println(input2.nextBoolean());
                    }
                    else if ( message.equals( "quit" ) )
                    {
                        output.println( message );
                        try
                        {
                            output.close();
                            output2.close();
                            input2.close();
                            input.close();
                            socket.close();
                            serverSocket.close();
                        }
                        catch ( IOException e )
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    output.flush();
                }
            }
        }
    }


    @Override
    public void actionPerformed( ActionEvent arg0 )
    {

    }
}
