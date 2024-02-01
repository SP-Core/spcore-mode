package spcore.appapi.commands;

import spcore.api.SpCoreApi;

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
            switch (action) {
                case "multyclick" -> PayMultyclick(manager);
                case "limit" -> PayEnergyLimit(manager);
                case "speed" -> PayRechangingSpeed(manager);
                case "energy" -> UseEnergy(manager);
            }

        }
        return false;
    }

    private void PriceList(SelectionManager manager){
        SpCoreApi.TREE.GetPriceList((p) -> {
            var builder = new StringBuilder();
            builder.append("\n");
            builder.append("balance: ").append(p.balance).append("t");
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
            manager.insert("\nОшибка получения данных\n");
        });
    }

    private void PayMultyclick(SelectionManager manager){
        SpCoreApi.TREE.PayMulticlickLevel(() -> {
            manager.selectAll();
            manager.delete(-1);
            manager.insert( "\nУспешно\n");

        }, () -> {
            manager.selectAll();
            manager.delete(-1);
            manager.insert("\nНедостаточно койнов\n");
        });
    }

    private void PayEnergyLimit(SelectionManager manager){
        SpCoreApi.TREE.PayEnergyLimitPrice(() -> {
            manager.selectAll();
            manager.delete(-1);
            manager.insert("\nУспешно\n");

        }, () -> {
            manager.selectAll();
            manager.delete(-1);
            manager.insert( "\nНедостаточно койнов\n");
        });
    }

    private void PayRechangingSpeed(SelectionManager manager){
        SpCoreApi.TREE.PayRechangingSpeed(() -> {
            manager.selectAll();
            manager.delete(-1);
            manager.insert( "\nУспешно\n");

        }, () -> {
            manager.selectAll();
            manager.delete(-1);
            manager.insert( "\nНедостаточно койнов\n");
        });
    }

    private void UseEnergy(SelectionManager manager){
        SpCoreApi.TREE.UseEnergy(() -> {
            manager.selectAll();
            manager.delete(-1);
            manager.insert( "\nУспешно\n");

        }, () -> {
            manager.selectAll();
            manager.delete(-1);
            manager.insert( "\nБусты закончились :(\n");
        });
    }

    @Override
    public String GetDescription() {
        return "TreeCoin";
    }
}
