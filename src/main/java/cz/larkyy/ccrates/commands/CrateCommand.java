package cz.larkyy.ccrates.commands;

import cz.larkyy.ccrates.CCrates;
import cz.larkyy.ccrates.objects.CrateInfo;
import cz.larkyy.ccrates.utils.CrateHandler;
import cz.larkyy.ccrates.utils.StorageUtils;
import cz.larkyy.ccrates.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CrateCommand {

    private CommandSender sender;
    private MainCommand mainCommand;
    private Utils utils;
    private CCrates main;
    private StorageUtils storageUtils;
    private final CrateHandler crateHandler;

    public CrateCommand(CommandSender sender, MainCommand mainCommand, String[] args) {
        this.sender = sender;
        this.mainCommand = mainCommand;
        this.main = mainCommand.getMain();
        this.utils = main.getUtils();
        this.storageUtils = main.getStorageUtils();
        this.crateHandler = main.getCrateHandler();

        if (mainCommand.isPlayerSender()) {
            Player p = (Player) sender;

            if (args.length < 2)
                return;

            switch (args[1]) {
                case "give":

                    if (args.length < 3)
                        return;

                    if (crateHandler.isCrateName(args[2])) {
                        p.sendMessage("Given crate item: " + args[2]);
                        CrateInfo crateInfo = crateHandler.getCrateInfo(args[2]);

                        p.getInventory().addItem(crateInfo.getCrateItem());
                    } else
                        p.sendMessage("Crate item doesn't exist: " + args[2]);
                    break;

                case "list":
                    break;
            }
        }


    }

}
