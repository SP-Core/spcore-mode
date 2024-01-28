package spcore.fabric.commands;

import spcore.fabric.commands.commandEngine.CommandEngine;
import spcore.fabric.spcore.SpCoreApi;
import net.minecraft.client.util.SelectionManager;

import java.util.HashMap;

public class TreeCommand extends BaseCommand{
    @Override
    public boolean Invoke(SelectionManager manager, HashMap<String, String> args) {
        var size = args.size();
        if(size == 0){
            PriceList(manager);
            return false;
        }

        if(size == 1){
            var action = args.get("1");
            if(action.equals("multyclick")){
                PayMultyclick(manager);
            }
            else if(action.equals("limit")){
                PayEnergyLimit(manager);
            }
            else if(action.equals("speed")){
                PayRechangingSpeed(manager);
            }
            else if(action.equals("energy")){
                manager.insert("Пока бусты не работают");
            }
        }
        return false;
    }

    private void PriceList(SelectionManager manager){
        SpCoreApi.GetPriceList((p) -> {
            var builder = new StringBuilder();
            builder.append(CommandEngine.MessageOpenTerminal());
            builder.append("\n");
            builder.append("balance: ").append(p.balance);
            builder.append("\n");
            builder.append("multyclick: ").append(p.multiclickLevel).append("lvl ").append(p.multiclickLevelPrice).append("t");
            builder.append("\n");
            builder.append("limit: ").append(p.energyLimitLevel).append("lvl ").append(p.energyLimitPrice).append("t");;
            builder.append("\n");
            builder.append("speed: ").append(p.rechangingSpeedLevel).append("lvl ");
            if(p.rechangingSpeedPrice == -1){
                builder.append("max");
            }
            else{
                builder.append(p.rechangingSpeedPrice).append("t");;
            }
            builder.append("\n");
            builder.append("Boosts:");
            builder.append("\n");
            builder.append("energy: ").append(p.fullEnergyBoostCount);
            builder.append("\n");
            manager.selectAll();
            manager.delete(-1);
            manager.insert(builder.toString());

        }, () -> {
            manager.selectAll();
            manager.delete(-1);
            manager.insert(CommandEngine.MessageOpenTerminal() + "\nОшибка\n");
        });
    }

    private void PayMultyclick(SelectionManager manager){
        SpCoreApi.PayMulticlickLevel(() -> {
            manager.selectAll();
            manager.delete(-1);
            manager.insert(CommandEngine.MessageOpenTerminal() + "\nУспешно\n");

        }, () -> {
            manager.selectAll();
            manager.delete(-1);
            manager.insert(CommandEngine.MessageOpenTerminal() + "\nНедостаточно койнов\n");
        });
    }

    private void PayEnergyLimit(SelectionManager manager){
        SpCoreApi.PayEnergyLimitPrice(() -> {
            manager.selectAll();
            manager.delete(-1);
            manager.insert(CommandEngine.MessageOpenTerminal() + "\nУспешно\n");

        }, () -> {
            manager.selectAll();
            manager.delete(-1);
            manager.insert(CommandEngine.MessageOpenTerminal() + "\nНедостаточно койнов\n");
        });
    }

    private void PayRechangingSpeed(SelectionManager manager){
        SpCoreApi.PayRechangingSpeed(() -> {
            manager.selectAll();
            manager.delete(-1);
            manager.insert(CommandEngine.MessageOpenTerminal() + "\nУспешно\n");

        }, () -> {
            manager.selectAll();
            manager.delete(-1);
            manager.insert(CommandEngine.MessageOpenTerminal() + "\nНедостаточно койнов\n");
        });
    }

    @Override
    public String GetDescription() {
        return "TreeCoin";
    }
}
