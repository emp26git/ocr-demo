package demo.scanner.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import demo.data.contact.ContactInfo;
import demo.parser.BusinessCardParser;
import demo.parser.BusinessCardParserImpl;

/**
 * Creates the UI used to collect the business card data. The data entry is performed using a JTextArea which
 * simulates the scanner. Once the text has been entered, clicking the "Scan" button will kick off the
 * BusinessCardParser to parse the text and attempt to find the contact person's name, phone, and email.
 * The results of parsing the data will be displayed in a pop-up dialog.
 */
public class ScannerUI extends JFrame {

    /** generated serial version ID */
    private static final long serialVersionUID = 7517066259609143622L;
    private static final Logger log = LogManager.getLogger(ScannerUI.class);

    private final JTextArea scanTextArea;
    private final JButton clearBtn;
    private final JButton scanBtn;


    /**
     * Launch the application.
     * @param args
     *      any command args for the application
     */
    public static void main(final String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    final ScannerUI frame = new ScannerUI();
                    frame.setVisible(true);
                }
                catch (final Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ScannerUI() {
        final BorderLayout borderLayout = (BorderLayout) getContentPane().getLayout();
        borderLayout.setHgap(10);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 500);
        setTitle("Business Card OCR Demo");

        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        getContentPane().add(mainPanel);

        final JPanel lblPanel = new JPanel(new BorderLayout());
        final JLabel lblEnterScanText = new JLabel("Enter Scan Text");
        lblEnterScanText.setHorizontalAlignment(SwingConstants.CENTER);
        lblEnterScanText.setFont(new Font("Tahoma", Font.PLAIN, 20));
        lblPanel.add(lblEnterScanText, BorderLayout.NORTH);
        mainPanel.add(lblPanel);

        scanTextArea = new JTextArea(15, 60);
        scanTextArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        scanTextArea.setMargin(new Insets(5, 5, 5, 5));
        lblEnterScanText.setLabelFor(scanTextArea);

        final JScrollPane textScrollPane = new JScrollPane(scanTextArea);
        textScrollPane.setBorder(new EmptyBorder(5, 10, 5, 10));
        lblPanel.add(textScrollPane, BorderLayout.CENTER);

        final JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        lblPanel.add(btnPanel, BorderLayout.SOUTH);
        mainPanel.add(lblPanel);

        clearBtn = new JButton("Clear");
        clearBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
        clearBtn.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        scanTextArea.setText("");
                    }

                });
        btnPanel.add(clearBtn);

        scanBtn = new JButton("Scan");
        scanBtn.setFont(new Font("Tahoma", Font.PLAIN, 16));
        scanBtn.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        final String submittedText = scanTextArea.getText();

                        if (log.isDebugEnabled()) {
                            log.debug("submitted text:\n" + submittedText);
                        }

                        // move this to a separate thread in order to not block UI execution
                        EventQueue.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    final BusinessCardParser parser = BusinessCardParserImpl.getInstance();
                                    final ContactInfo contactInfo = parser.getContactInfo(submittedText);

                                    if (log.isDebugEnabled()) {
                                        log.debug("generated ContactInfo=" + contactInfo);
                                    }

                                    String message = "No ContactInfo created.";
                                    if (contactInfo != null) {
                                        final StringBuilder msgBuf = new StringBuilder("Name: ");

                                        msgBuf.append(contactInfo.getName()).append("\nPhone: ").append(contactInfo.getPhoneNumber())
                                            .append("\nEmail: ").append(contactInfo.getEmailAddress());
                                        message = msgBuf.toString();

                                    }
                                    JOptionPane.showMessageDialog(mainPanel, message);
                                }
                                catch (final Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });

                    }
                });
        btnPanel.add(scanBtn);

    }

}
