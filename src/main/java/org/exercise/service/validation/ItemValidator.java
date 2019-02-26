package org.exercise.service.validation;

import org.exercise.service.model.Item;
import org.exercise.service.model.ItemLocationStock;
import org.exercise.service.model.Location;

import java.text.MessageFormat;
import java.util.Map;

public class ItemValidator
{
    private Map<Integer, Location> locations;

    public ItemValidator(Map<Integer, Location> locations)
    {
        this.locations = locations;
    }

    public void validate(Item item) throws ValidationException
    {
        if (item.getPrice() < 0)
        {
            throw new ValidationException("Price can't be negative.");
        }

        for(ItemLocationStock locationStock : item.getItemLocationStocks())
        {
            if (locationStock.getStock() < 0 )
            {
                throw new ValidationException("Location stock can't be negative");
            }

             int locationId = locationStock.getLocation().getId();
            if (locations.get(locationId) == null)
            {
                throw new ValidationException(MessageFormat.format("Location with id {0} not found.", locationId));
            }
        }

    }
}
