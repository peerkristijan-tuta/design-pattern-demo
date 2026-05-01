import java.util.*;

// Creational Patterns
class Singleton {
    private static Singleton instance = null;
    private Singleton() {}
    static Singleton getInstance() {
        if (instance == null)
            return instance = new Singleton();
        else
            return instance;
    }

    void execute() {
        System.out.print("Singleton classes can only be instantiated once.");
    }
}

class FactoryMethod {
    abstract class Sentence {
        String content = null;

        Sentence retrieveSentence() {
            Sentence sentence;

            sentence = createSentence();

            sentence.addFooter();
            sentence.addHeader();

            return sentence;
        }

        abstract Sentence createSentence();

        void addHeader() {
            content =  "-----[header]--------------\n" + content;
        }

        void addFooter() {
            content = content + "\n-----[footer]--------------";
        }
    }

    class Definition extends Sentence{
        Sentence createSentence() {
            Definition definition = new Definition();
            definition.content = "A factory method instantiates all subclasses in the same way.";
            return definition;
        }
    }

    void execute() {
        System.out.print(new Definition().retrieveSentence().content);
    }
}

// Structural Patterns
class Composite {
    void execute() {
        abstract class PrintSentence {
            abstract void print();
        }

        class SubSentence extends PrintSentence {
            String content = "";

            SubSentence(String content) {
                this.content = content;
            }

            void print() {
                System.out.print(content);
            }
        }

        class Sentence extends PrintSentence {
            PrintSentence[] subSentences = new PrintSentence[2];

            Sentence() {
                subSentences[0] = new SubSentence("Composite classes are those that");
                subSentences[1] = new SubSentence("consist of each other.");
            }

            void print() {
                subSentences[0].print();
                System.out.print(" ");
                subSentences[1].print();
            }
        }

        PrintSentence printSentence = new Sentence();
        printSentence.print();
    }
}

class Decorator {
    void execute() {
        abstract class PrintedText {
            abstract void display();
        }

        class Text extends PrintedText {
            void display() {
                System.out.println("The decorator allows objects to be nested within objects with the same primordial superclass so that they can be executed in nested fashion.");
            }
        }

        abstract class PrintedTextDecorator extends PrintedText {
            PrintedText nextElement = null;
            void display() {
                nextElement.display();
            }
            PrintedTextDecorator(PrintedText nextElement) {
                this.nextElement = nextElement;
            }
            abstract void install();
        }

        class Header extends PrintedTextDecorator {
            Header(PrintedText nextElement) {
                super(nextElement);
            }

            void display() {
                install();
                super.display();
            }

            void install() {
                System.out.println("-------------------[Header]-----------------------------------------------------------------------------------------------------------------");
            }
        }

        class Footer extends PrintedTextDecorator {
            Footer(PrintedText nextElement) {
                super(nextElement);
            }

            void display() {
                super.display();
                install();
            }

            void install() {
                System.out.println("-------------------[Footer]-----------------------------------------------------------------------------------------------------------------");
            }
        }

        PrintedText element = new Text();
        element = new Footer(element);
        element = new Header(element);
        element.display();
    }
}

class Adapter { 
    void execute() {
        class TextService {
            String request(int request) {
                if (request == 1) {
                    return "The adapter allows two objects with incompatible interfaces to connect to each other.";
                }

                return null;
            }
        }

        abstract class TextRequester {
            abstract String request(String query);
        }

        class TextAdapter extends TextRequester {
            TextService textService = new TextService();
            String request(String query) {
                    return textService.request(queryToNum(query));
            }
            int queryToNum(String query) {
                if (query.equals("definition"))
                    return 1;

                return 0;
            }
        }

        class TextClient {
            void execute() {
                TextRequester requester = new TextAdapter();
                System.out.print(requester.request("definition"));
            }
        }

        new TextClient().execute();
    }
}

class Proxy {
    void execute() {
        abstract class Compute {
            abstract void fulfillComputation(String[] expression);
        }

        class Addition extends Compute {
            void fulfillComputation(String[] expression) {
                System.out.print(
                    expression[0] + " " + expression[1] + " " + 
                    expression[2] + " is " + 
                    (Integer.valueOf(expression[0]) + Integer.valueOf(expression[2]))
                );
            }
        }

        class Subtraction extends Compute {
            void fulfillComputation(String[] expression) {
                System.out.print(
                    expression[0] + " " + expression[1] + " " + 
                    expression[2] + " is " + 
                    (Integer.valueOf(expression[0]) - Integer.valueOf(expression[2]))
                );
            }
        }

        class ComputationFulfillment extends Compute {
            Compute[] algorithms = {
                new Addition(),
                new Subtraction()
            };

            void fulfillComputation(String[] expression) {
                if (expression[1].equals("+"))
                    algorithms[0].fulfillComputation(expression);
                else
                    algorithms[1].fulfillComputation(expression);
            }
        }

        Compute compute = new ComputationFulfillment();
        compute.fulfillComputation(new String[] {"2", "+", "2"});
    }
}

class Facade {
    void execute() {
        abstract class Account {
            int amount;
            Account(int amount) {
                this.amount = amount;
            }
        }

        class Saving extends Account {
            Saving(int amount) {
                super(amount);
            }
        }

        class Investment extends Account {
            Investment(int amount) {
                super(amount);
            }
        }

        class BankService {
            Account[] accounts = null;
            BankService() {
                accounts = new Account[] {
                    new Saving(500),
                    new Investment(1000)
                };
            };

            void enumerateSavingToInvestmentRatio() {
                System.out.print(Double.valueOf(accounts[1].amount) / Double.valueOf(accounts[0].amount));
            }
        }

        BankService service = new BankService();
        service.enumerateSavingToInvestmentRatio();
    }
}

// Behavioral patterns
class ChainOfResponsibility {
    void execute() {
        abstract class Handler {
            // This is also a template method
            void handleRequest(String[] expression) {
                if (!determineEligibility(expression))
                    escalate(expression);
                else
                    solve(expression);
            }

            abstract boolean determineEligibility(String[] expression);
            abstract void solve(String[] expression);
            abstract void escalate(String[] expression);
        }

        class MultiplicationHandler extends Handler {
            boolean determineEligibility(String[] expression) {
                if (expression[1].equals("x"))
                    return true;
                else return false;
            }

            void escalate(String[] expression) {
                System.out.print("Invalid operator");
            }

            void solve(String[] expression) {
                System.out.print(expression[0] + " x "
                                 + expression[2] + " = "
                                 + (Integer.valueOf(expression[0]) * Integer.valueOf(expression[2]))
                                );
            }
        }

        class SubtractionHandler extends Handler {
            boolean determineEligibility(String[] expression) {
                if (expression[1].equals("-"))
                    return true;
                else return false;
            }

            void escalate(String[] expression) {
                new MultiplicationHandler().handleRequest(expression);
            }

            void solve(String[] expression) {
                System.out.print(expression[0] + " - "
                                 + expression[2] + " = "
                                 + (Integer.valueOf(expression[0]) - Integer.valueOf(expression[2]))
                                );
            }
        }

        class AdditionHandler extends Handler {
            boolean determineEligibility(String[] expression) {
                if (expression[1].equals("+"))
                    return true;
                else return false;
            }

            void escalate(String[] expression) {
                new SubtractionHandler().handleRequest(expression);
            }

            void solve(String[] expression) {
                System.out.print(expression[0] + " + "
                                 + expression[2] + " = "
                                 + (Integer.valueOf(expression[0]) + Integer.valueOf(expression[2]))
                                );
            }
        }

        Handler handler = new AdditionHandler();
        handler.handleRequest(new String[] {
            "5",
            "x",
            "2"
        });
    }
}

class State {
    void execute() {
        abstract class AbstractState {
            abstract void handle(String[] expression);
        }

        class Addition extends AbstractState {
            void handle(String[] expression) {
                System.out.print(expression[0] + " + " + expression[2] + " = " + (Integer.valueOf(expression[0]) + Integer.valueOf(expression[2])));
            }
        }

        class Subtraction extends AbstractState {
            void handle(String[] expression) {
                System.out.print(expression[0] + " - " + expression[2] + " = " + (Integer.valueOf(expression[0]) - Integer.valueOf(expression[2])));
            }
        }

        class Context {
            AbstractState state = null;
            AbstractState[] states = {
                new Addition(),
                new Subtraction()
            };

            void request(String[] expression) {
                if (expression[1].equals("+"))
                    state = states[0];
                else
                    state = states[1];

                state.handle(expression);
            }
        }

        new Context().request(new String[] {
            "5",
            "-",
            "3"
        });
    }  
}

class Mediator {
    void execute() {
        abstract class MediatorEntity {
            ColleagueEntity[] colleagues = null;
            String definition = null;
            abstract String requestDefinition();
            
            MediatorEntity() {
                colleagues = new ColleagueEntity[] {new Colleague1(this), new Colleague2(this)};
            }

            static abstract class ColleagueEntity {
                MediatorEntity mediator = null;
                ColleagueEntity(MediatorEntity mediator) {
                    this.mediator = mediator;
                }
                abstract void mediatorTrigger();
            }

            static class Colleague1 extends ColleagueEntity {
                Colleague1(MediatorEntity mediator) {
                    super(mediator);
                }

                void mediatorTrigger() {
                    System.out.print(mediator.requestDefinition());
                }
            }

            static class Colleague2 extends ColleagueEntity {
                String definition = "Mediator classes interface all other classes.";

                Colleague2(MediatorEntity mediator) {
                    super(mediator);
                }

                void mediatorTrigger() {
                    mediator.definition = definition;
                }
            }
        }

        class Mediator1 extends MediatorEntity {
            String requestDefinition() {
                colleagues[1].mediatorTrigger();
                return definition;
            }

            void execute() {
                colleagues[0].mediatorTrigger();
            }
        }

        new Mediator1().execute();
    }
}

class Command {
    abstract class CommandEntity {
        abstract void execute(String[] command);
        abstract void unexecute();
    }

    class Command1 extends CommandEntity {
        class Title {
            String content = "";
            void display() {
                System.out.println("Title: " + content);
            }
        }

        class Description {
            String content = "";
            void display() {
                System.out.println("Description: " + content + "\n");
            }
        }

        ArrayList<String[]> commandBank = new ArrayList<String[]>();
        Title t = new Title();
        Description d = new Description();
        void execute(String[] command) {
            if (command[0].equals("update")) {
                if (command[1].equals("t")) {
                    commandBank.add(new String[] {
                        "update",
                        "t",
                        t.content
                    });
                    t.content = command[2];
                } else {
                    commandBank.add(new String[] {
                        "update",
                        "d",
                        d.content
                    });
                    d.content = command[2];
                }
            } else {
                t.display();
                d.display();
            }
        }

        void unexecute() {
            if (commandBank.size() > 0) {
                String[] command = commandBank.toArray(new String[0][])[commandBank.size()-1];
                if (command[1].equals("t"))
                    t.content = command[2];
                else
                    d.content = command[2];
                commandBank.remove(commandBank.size()-1);
            } else {
                System.out.println("No more steps to undo\n");
            }
        }
    }

    class CommandManager {
        Command1 c = new Command1();
        void demonstrate() {
            c.execute(new String[] {
                "update",
                "t",
                "Command"
            });

            c.execute(new String[] {
                "update",
                "d",
                "Allows for doing and undoing activities"
            });

            c.execute(new String[] {
                "display"
            });

            c.execute(new String[] {
                "update",
                "d",
                "Is excuted through a Command object"
            });

            c.execute(new String[] {
                "display"
            });

            for (int x = 0; x < 4; x++) {
                c.unexecute();
                c.execute(new String[] {
                    "display"
                });
            }
        }
    }

    void execute() {
        new CommandManager().demonstrate();
    }
}

class Observer {
    class Listener extends Thread {
        Responder r = null;
        boolean trigger = false;
        void subscribe() {
            r = new Responder(this);
            r.start();
        }

        void customGetState() {
            System.out.println(r.message);
            trigger = false;
        }

        void unsubscribe() {
            r.subscription = false;
        }

        public void run() {
            subscribe();
            
            while (true) {
                try {
                    sleep(10);
                } catch (Exception exc) {}
                if (trigger)
                    break;
            }

            customGetState();

            while (true) {
                try {
                    sleep(10);
                } catch (Exception exc) {}

                if (trigger)
                    break;
            }

            customGetState();
            unsubscribe();
        }
    }

    class Responder extends Thread {
        Listener l = null;
        int increment = 0;
        Boolean subscription = null;
        String message = null;
        String[] definition = {
            "Observer",
            "Systematic subscription-based data exchange"
        };
        
        Responder(Listener l) {
            this.l = l;
            subscription = true;
        }

        void customNotify() {
            while (true) {
                l.trigger = true;
                try {
                    sleep(10);
                } catch (Exception exc) {}

                if (!l.trigger)
                    break;
            }
        }

        void update() {
            message = definition[increment++];
        }

        void continueNormalOperations() {}

        public void run() {
            update();
            customNotify();
            update();
            customNotify();

            while (true) {
                continueNormalOperations();
                try {
                    sleep(10);
                } catch (Exception exc) {}
                if (!subscription)
                    break;
            }
        }
    }

    void execute() {
        new Listener().start();
    }
}

class DesignPatterns {
    public static void main(String[] args) {
        switch (args[0]) {
            case "singleton":
                Singleton.getInstance().execute();
                break;
            case "factoryMethod":
                new FactoryMethod().execute();
                break;
            case "composite":
                new Composite().execute();
                break;
            case "decorator":
                new Decorator().execute();
                break;
            case "adapter":
                new Adapter().execute();
                break;
            case "proxy":
                new Proxy().execute();
                break;
            case "facade":
                new Facade().execute();
                break;
            case "chainOfResponsibility":
                new ChainOfResponsibility().execute();
                break;
            case "state":
                new State().execute();
                break;
            case "mediator":
                new Mediator().execute();
                break;
            case "command":
                new Command().execute();
                break;
            case "observer":
                new Observer().execute();
                break;
        }
    }
}